import { PizzaResponse } from './PizzaResponse';

export interface CartItemResponse {
	id: number;
	pizza: PizzaResponse;
	cartId: number;
	amount: number;
}
