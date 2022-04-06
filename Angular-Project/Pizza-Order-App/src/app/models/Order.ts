import { Address } from './Address';

export class Order {
	private _id: number;

	private _date: string;

	private _address: Address;

	private _total: number;

	private _status: string;

	constructor(
		id: number,
		date: string,
		address: Address,
		total: number,
		status: string
	) {
		this._id = id;
		this._date = date;
		this._address = address;
		this._total = total;
		this._status = status;
	}

	get id(): number {
		return this._id;
	}

	get date(): string {
		return this._date;
	}

	get address(): Address {
		return this._address;
	}

	get total(): number {
		return this._total;
	}

	get status(): string {
		return this._status;
	}
}
