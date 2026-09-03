export default interface PageResponse<T> {
    content: T[]; // data
    totalPages: number;
    totalElements: number;
    page: number,
    size: number;
    sort?: string | string[]
}