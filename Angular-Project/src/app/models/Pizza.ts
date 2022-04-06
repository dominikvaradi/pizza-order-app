export class Pizza {
	private _id: number;

	private _name: string;

	private _description: string;

	private _imageUrl: string;

	private _price: number;

	constructor(
		id: number,
		name: string,
		description: string,
		imageUrl: string,
		price: number
	) {
		this._id = id;
		this._name = name;
		this._description = description;
		this._imageUrl = imageUrl;
		this._price = price;
	}

	get id(): number {
		return this._id;
	}

	get name(): string {
		return this._name;
	}

	get description(): string {
		return this._description;
	}

	get imageUrl(): string {
		return this._imageUrl;
	}

	get price(): number {
		return this._price;
	}
}
