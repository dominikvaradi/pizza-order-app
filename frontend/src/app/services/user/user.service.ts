import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
	HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, observable, Observable } from 'rxjs';
import { AddressCreateRequest } from 'src/app/models/AddressCreateRequest';
import { AddressResponse } from 'src/app/models/AddressResponse';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { PageResponse } from 'src/app/models/PageResponse';
import { UserEditRequest } from 'src/app/models/UserEditRequest';
import { UserRegisterRequest } from 'src/app/models/UserRegisterRequest';
import { UserResponse } from 'src/app/models/UserResponse';
import { environment } from 'src/environments/environment';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { EventBusService } from '../event-bus/event-bus.service';

const USER_SERVICE_URL = environment.apiUrl + '/users';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class UserService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	public registerUser(request: UserRegisterRequest): Observable<UserResponse> {
		return this.http.post<UserResponse>(USER_SERVICE_URL, request, httpOptions);
	}

	public getUserById(userId: number): Observable<UserResponse> {
		return this.http
			.get<UserResponse>(USER_SERVICE_URL + `/${userId}`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getUsersPaginated(
		page: number,
		pageSize: number,
		term: string
	): Observable<PageResponse<UserResponse>> {
		const params = new HttpParams()
			.set('page', page)
			.set('size', pageSize)
			.set('term', term);

		return this.http
			.get<PageResponse<UserResponse>>(USER_SERVICE_URL, {
				params: params,
			})
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public editUser(userEditRequest: UserEditRequest): Observable<UserResponse> {
		return this.http
			.put<UserResponse>(
				USER_SERVICE_URL + `/${userEditRequest.id}`,
				userEditRequest,
				httpOptions
			)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public deleteUser(userId: number): Observable<any> {
		return this.http
			.delete(USER_SERVICE_URL + `/${userId}`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getAddressesByUserId(userId: number): Observable<AddressResponse[]> {
		return this.http
			.get<AddressResponse[]>(USER_SERVICE_URL + `/${userId}/addresses`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public createAddress(
		addressCreateRequest: AddressCreateRequest
	): Observable<AddressResponse> {
		return this.http
			.post<AddressResponse>(
				USER_SERVICE_URL + `/${addressCreateRequest.userId}/addresses`,
				addressCreateRequest,
				httpOptions
			)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getOrdersByUserIdPaginated(
		userId: number,
		page: number,
		pageSize: number
	): Observable<PageResponse<OrderResponse>> {
		const params = new HttpParams().set('page', page).set('size', pageSize);

		return this.http
			.get<PageResponse<OrderResponse>>(USER_SERVICE_URL + `/${userId}/orders`, {
				params: params,
			})
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getActiveOrderByUserId(
		userId: number,
		withItems: boolean = false
	): Observable<OrderResponse> {
		const params = new HttpParams().set('withItems', withItems);

		return this.http
			.get<OrderResponse>(USER_SERVICE_URL + `/${userId}/active-order`, {
				params: params,
			})
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
