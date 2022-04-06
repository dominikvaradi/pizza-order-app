import { Order } from './Order';
import { Pizza } from './Pizza';

export class OrderWithInformationAndPizzas {
	private _order: Order;

	private _pizzas: Pizza[];

	private _fullName: string;

	private _phoneNumber: number;

	constructor(
		order: Order,
		pizzas: Pizza[],
		fullName: string,
		phoneNumber: number
	) {
		this._order = order;
		this._pizzas = pizzas;
		this._fullName = fullName;
		this._phoneNumber = phoneNumber;
	}

	get order(): Order {
		return this._order;
	}

	get pizzas(): Pizza[] {
		return this._pizzas;
	}

	get fullName(): string {
		return this._fullName;
	}

	get phoneNumber(): number {
		return this._phoneNumber;
	}
}
