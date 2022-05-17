import {
	HttpErrorResponse,
	HttpEvent,
	HttpHandler,
	HttpInterceptor,
	HttpRequest,
	HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Injectable, Provider } from '@angular/core';
import {
	BehaviorSubject,
	catchError,
	filter,
	Observable,
	of,
	switchMap,
	take,
	throwError,
} from 'rxjs';
import { EventData } from '../../models/EventData';
import { RefreshTokenRequest } from '../../models/RefreshTokenRequest';
import { RefreshTokenResponse } from '../../models/RefreshTokenResponse';
import { AuthService } from './auth.service';
import { EventBusService } from '../event-bus/event-bus.service';
import { UserDetailsStorageService } from './user-details-storage.service';

const TOKEN_HEADER_KEY: string = 'Authorization';
const TOKEN_PREFIX_NAME: string = 'Bearer';

@Injectable()
export class JwtAuthHttpInterceptor implements HttpInterceptor {
	private isRefreshing = false;

	private refreshTokenSubject: BehaviorSubject<string | null> =
		new BehaviorSubject<string | null>(null);

	constructor(
		private userDetailsStorageService: UserDetailsStorageService,
		private authService: AuthService
	) {}

	intercept(
		req: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<Object>> {
		let authReq = req;

		const token = this.userDetailsStorageService.getToken();
		if (token != null) {
			authReq = this.addTokenHeader(req, token);
		}

		return next.handle(authReq).pipe(
			catchError((error: HttpErrorResponse) => {
				if (
					!authReq.url.includes('auth/login') &&
					!authReq.url.includes('auth/refresh-token') &&
					error.status === 401
				) {
					return this.handle401Error(authReq, next);
				}
				return throwError(() => error);
			})
		);
	}

	private handle401Error(
		request: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {
		if (!this.isRefreshing) {
			this.isRefreshing = true;
			this.refreshTokenSubject.next(null);

			const token = this.userDetailsStorageService.getRefreshToken();
			if (token) {
				const refreshTokenRequest: RefreshTokenRequest = {
					refreshToken: token,
				};

				return this.authService.refreshToken(refreshTokenRequest).pipe(
					switchMap((response: RefreshTokenResponse) => {
						this.isRefreshing = false;
						this.userDetailsStorageService.saveToken(response.accessToken);
						this.refreshTokenSubject.next(response.accessToken);

						return next.handle(this.addTokenHeader(request, response.accessToken));
					}),
					catchError((error: HttpErrorResponse) => {
						this.isRefreshing = false;

						return throwError(() => error);
					})
				);
			}
		}

		return this.refreshTokenSubject.pipe(
			filter((token: string | null) => token !== null),
			take(1),
			switchMap((token: string | null) =>
				next.handle(this.addTokenHeader(request, token as string))
			)
		);
	}

	private addTokenHeader(request: HttpRequest<any>, token: string) {
		return request.clone({
			headers: request.headers.set(
				TOKEN_HEADER_KEY,
				TOKEN_PREFIX_NAME + ' ' + token
			),
		});
	}
}

export function unauthorizedHandler(
	error: HttpErrorResponse,
	eventBusService: EventBusService
): Observable<never> {
	if (error.status === 401 && error.url?.includes('auth/refresh-token')) {
		eventBusService.emit(new EventData('logout', null));
	}

	return throwError(() => error);
}

export const jwtAuthHttpInterceptorProviders: Provider[] = [
	{ provide: HTTP_INTERCEPTORS, useClass: JwtAuthHttpInterceptor, multi: true },
];
