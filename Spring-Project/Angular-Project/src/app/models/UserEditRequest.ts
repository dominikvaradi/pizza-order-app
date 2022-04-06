export class UserEditRequest {
	private _id: number;

	private _username: string;

	private _fullName: string;

	private _email: string;

	private _phoneNumber: number;

	private _password: string;

	constructor(
		id: number,
		username: string,
		fullName: string,
		email: string,
		phoneNumber: number,
		password: string
	) {
		this._id = id;
		this._username = username;
		this._fullName = fullName;
		this._email = email;
		this._phoneNumber = phoneNumber;
		this._password = password;
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

	get password(): string {
		return this._password;
	}

	set(propertyName: string, value: string) {
		switch (propertyName) {
			case 'id':
				this._id = parseInt(value);
				break;
			case 'username':
				this._username = value;
				break;
			case 'email':
				this._email = value;
				break;
			case 'fullName':
				this._fullName = value;
				break;
			case 'phoneNumber':
				this._phoneNumber = parseInt(value);
				break;
			case 'password':
				this._password = value;
				break;
		}
	}
}
