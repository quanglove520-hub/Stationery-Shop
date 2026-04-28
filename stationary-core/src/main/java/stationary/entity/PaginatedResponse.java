package stationary.entity;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public PaginatedResponse(List<T> data, int currentPage, int totalPages, long totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<T> getData() { return data; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
    public long getTotalItems() { return totalItems; }
}
