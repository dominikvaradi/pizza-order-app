import { UserResponse } from "./UserResponse";

export interface JwtResponse {
	token: string;
	refreshToken: string;
	tokenType: string;
	user: UserResponse
}
