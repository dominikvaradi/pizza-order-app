export class PizzaEditRequest {
	private _id: number;

	private _name: string;

	private _description: string;

	private _image: File;

	private _price: number;

	constructor(
		id: number,
		name: string,
		description: string,
		image: File,
		price: number
	) {
		this._id = id;
		this._name = name;
		this._description = description;
		this._image = image;
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

	get image(): File {
		return this._image;
	}

	get price(): number {
		return this._price;
	}
}
