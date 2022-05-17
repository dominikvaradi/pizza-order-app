export interface UserEditRequest {
	id: number;
	username?: string;
	password?: string;
	email?: string;
	phoneNumber?: string;
	fullName?: string;
	roleName?: string;
}
