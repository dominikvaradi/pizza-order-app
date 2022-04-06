export class Address {
	private _id: number;

	private _zipCode: number;

	private _city: string;

	private _street: string;

	private _number: string;

	constructor(
		id: number,
		zipCode: number,
		city: string,
		street: string,
		number: string
	) {
		this._id = id;
		this._zipCode = zipCode;
		this._city = city;
		this._street = street;
		this._number = number;
	}

	get id(): number {
		return this._id;
	}

	get zipCode(): number {
		return this._zipCode;
	}

	get city(): string {
		return this._city;
	}

	get street(): string {
		return this._street;
	}

	get number(): string {
		return this._number;
	}

	toString(): string {
		return (
			this._zipCode + ' ' + this._city + ', ' + this._street + ' ' + this._number
		);
	}
}
