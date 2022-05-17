import { AddressResponse } from './AddressResponse';
import { OrderItemResponse } from './OrderItemResponse';

export interface OrderResponse {
	id: number;
	createdAt: Date;
	orderStatusName: string;
	userId: number;
	username: string;
	address: AddressResponse;
	paymentMethodName: string;
	items: OrderItemResponse[];
	totalSum: number;
}
