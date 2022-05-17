import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { JwtResponse } from '../../models/JwtResponse';
import { LoginRequest } from '../../models/LoginRequest';
import { RefreshTokenRequest } from '../../models/RefreshTokenRequest';
import { RefreshTokenResponse } from '../../models/RefreshTokenResponse';
import { RegisterRequest } from '../../models/RegisterRequest';
import { UserResponse } from '../../models/UserResponse';
import { EventBusService } from '../event-bus/event-bus.service';
import { unauthorizedHandler } from './JwtAuthHttpInterceptor';

const AUTH_SERVICE_URL = environment.apiUrl + '/auth';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class AuthService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	login(loginRequest: LoginRequest): Observable<JwtResponse> {
		return this.http.post<JwtResponse>(
			AUTH_SERVICE_URL + '/login',
			loginRequest,
			httpOptions
		);
	}

	register(registerRequest: RegisterRequest): Observable<UserResponse> {
		return this.http.post<UserResponse>(
			AUTH_SERVICE_URL + '/register',
			registerRequest,
			httpOptions
		);
	}

	refreshToken(
		refreshTokenRequest: RefreshTokenRequest
	): Observable<RefreshTokenResponse> {
		return this.http.post<RefreshTokenResponse>(
			AUTH_SERVICE_URL + '/refresh-token',
			refreshTokenRequest,
			httpOptions
		);
	}
}
