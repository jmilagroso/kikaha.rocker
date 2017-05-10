package myapp.routes;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import com.google.common.primitives.Ints;
import kikaha.core.modules.http.WebResource;
import me.lemire.integercompression.*;
import myapp.models.BufferEncoder;
import myapp.models.FastByteArrayOutputStream;
import myapp.models.Probe;


import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Singleton
@WebResource( path="/fastpfor", method="GET" )
public class FastPForResource implements HttpHandler {
    private final static NumberFormat nf = NumberFormat.getInstance();


    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 150000 );
        BufferEncoder e = new BufferEncoder(fastByteArrayOutputStream);

        double value = 29.33666687011719d;
        double round = Math.round(value * 100.0) / 100.0;
        int asInt = (int)(round * 100.0);
        double backToDouble = asInt / 100.0;

        List<Integer> probeList = new ArrayList<>();

        int numberOfProbes = 50000;

        for (int i = 0; i < numberOfProbes; i++) {
//            2               = sex
//            39              = age
//            2934            = x
//            2262            = y
//            51521243        = lat
//            21821245        = lng
//            0               = associated
            probeList.add(randomRange(0, 1)); //associated
            probeList.add(randomRange(0, 2)); //sex
            probeList.add(randomRange(0, 65)); //age
            probeList.add(randomRange(2000, 3000)); //x
            probeList.add(randomRange(2000, 3000)); //y
            probeList.add(randomRange(500000, 550000)); //lat
            probeList.add(randomRange(500000, 550000)); //lng
        }

        int[] probes = Ints.toArray(probeList);

        List<Probe> myProbes = new ArrayList<Probe>();

        for (int i = 0; i < probes.length; i+=7) {
//            0               = associated
//            2               = sex
//            39              = age
//            2934            = x
//            2262            = y
//            51521243        = lat
//            21821245        = lng
            int associated = probes[i];
            int sex = probes[i+1];
            int age = probes[i+2];
            int x = probes[i+3];
            int y = probes[i+4];
            int lat = probes[i+5];
            int lng = probes[i+6];
            System.out.println(
                    " sex=" + sex +
                            " age=" + age +
                            " x=" + x / 100d +
                            " y=" + y / 100d +
                            " lat=" + lat / 10000d +
                            " lng=" + lng / 10000d +
                            " associated=" + associated
            );

            Probe p = new Probe();
            p.sex = sex;
            p.age = age;
            p.x = x;
            p.y = y;
            p.lat = lat;
            p.lng = lng;
            p.associated = associated;

            myProbes.add(p);

        }

        IntegerCODEC codec =  new
                Composition(
                new FastPFOR(),
                new VariableByte());
        // compressing
        IntWrapper inputoffset = new IntWrapper(0);
        IntWrapper outputoffset = new IntWrapper(0);
        int[] compressed = new int [probes.length - 256];// could need more
        codec.compress(probes,inputoffset,probes.length,compressed,outputoffset);

        int cLen = outputoffset.intValue();
        int[] compressedExact = new int [cLen];// could need more
        System.arraycopy(compressed, 0, compressedExact, 0, cLen);
        //System.out.println();

        int[] roundtrip = new int[probes.length];
        IntWrapper recoffset = new IntWrapper(0);
        codec.uncompress(compressedExact,new IntWrapper(0),compressedExact.length,roundtrip,recoffset);
        if(Arrays.equals(probes,roundtrip))
            System.out.println("data is recovered without loss");
        else
            throw new RuntimeException("bug"); // could use assert
        System.out.println();

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/jwk-set+json;charset=UTF-8");

        exchange.getResponseSender().send(e.encode(myProbes));
    }

    public static int randomRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private final static Random random = getSecureRandom();

    private static Random getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




}
