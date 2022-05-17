import { OrderResponse } from './OrderResponse';

export interface PageResponse<T> {
	content: T[];
	pageable: string;
	last: boolean;
	totalElements: number;
	totalPages: number;
	size: number;
	number: number;
	sort: SortResponse;
	first: boolean;
	numberOfElements: number;
	empty: boolean;
}

export interface SortResponse {
	empty: boolean;
	sorted: boolean;
	unsorted: boolean;
}
