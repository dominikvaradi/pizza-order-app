export class UserWithGroup {
	private _id: number;

	private _username: string;

	private _fullName: string;

	private _email: string;

	private _phoneNumber: number;

	private _group: string;

	constructor(
		id: number,
		username: string,
		fullName: string,
		email: string,
		phoneNumber: number,
		group: string
	) {
		this._id = id;
		this._username = username;
		this._fullName = fullName;
		this._email = email;
		this._phoneNumber = phoneNumber;
		this._group = group;
	}

	get id(): number {
		return this._id;
	}

	get username(): string {
		return this._username;
	}

	get fullName(): string {
		return this._fullName;
	}

	get email(): string {
		return this._email;
	}

	get phoneNumber(): number {
		return this._phoneNumber;
	}

	get group(): string {
		return this._group;
	}
}
