package kikaha.app.services;

/**
 * Created by jay on 5/10/17.
 */
public class Paginator {

    // The number of rows/items per page. (Page 1...N has 5 items display)
    public Integer perPage;
    // The size of data.
    public Integer dataSize;
    // The number of pages in the paginator navigation.
    public Integer pageCount;
    // The current page.
    public Integer currentPage;
    // The start index reference in the subList.
    public Integer startIndex;
    // The end index reference in the subList.
    public Integer endIndex;

    /**
     * Paginator
     * @param perPage  - The number of rows/items per page. (Page 1...N has 5 items display)
     * @param dataSize - The size of data.
     * @param page     - The current page.
     */
    public void handle(Integer perPage, Integer dataSize, Integer page) {
        this.perPage = perPage;
        this.dataSize = dataSize;
        this.currentPage = (page > 1) ? page : 1;

        this.pageCount = (int) Math.ceil((float)this.dataSize/this.perPage);

        this.startIndex = ((this.perPage*this.currentPage) - this.perPage);
        this.endIndex   = (this.startIndex + (this.perPage));
    }
}
