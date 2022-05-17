import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { UserResponse } from 'src/app/models/UserResponse';
import { JwtResponse } from '../../models/JwtResponse';

const TOKEN_KEY = 'auth-token';
const REFRESH_TOKEN_KEY = 'auth-refresh-token';
const USER_KEY = 'user';

@Injectable({
	providedIn: 'root',
})
export class UserDetailsStorageService {
	constructor() {}

	public logout(): void {
		window.localStorage.clear();
	}

	public saveToken(token: string): void {
		window.localStorage.removeItem(TOKEN_KEY);
		window.localStorage.setItem(TOKEN_KEY, token);
	}

	public getToken(): string | null {
		return window.localStorage.getItem(TOKEN_KEY);
	}

	public saveUser(user: UserResponse) {
		window.localStorage.removeItem(USER_KEY);
		window.localStorage.setItem(USER_KEY, JSON.stringify(user));
	}

	public getUser(): UserResponse | null {
		const user = window.localStorage.getItem(USER_KEY);
		if (user === null) return null;

		return JSON.parse(user) as UserResponse;
	}

	public saveRefreshToken(token: string): void {
		window.localStorage.removeItem(REFRESH_TOKEN_KEY);
		window.localStorage.setItem(REFRESH_TOKEN_KEY, token);
	}

	public getRefreshToken(): string | null {
		return window.localStorage.getItem(REFRESH_TOKEN_KEY);
	}

	isUserLoggedIn(): boolean {
		return (
			this.getToken() !== null &&
			this.getUser() !== null &&
			this.getRefreshToken() !== null
		);
	}
}
