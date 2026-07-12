export default interface PageResponse<T> {
    content: T[]; // data
    totalPages: number;
    totalElements: number;
    size: number;
    number: number; // current page
}