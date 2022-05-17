import { PizzaResponse } from './PizzaResponse';

export interface OrderItemResponse {
	id: number;
	pizza: PizzaResponse;
	orderId: number;
	amount: number;
}
