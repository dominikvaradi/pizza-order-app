import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
	HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { OrderCreateRequest } from 'src/app/models/OrderCreateRequest';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { OrderStatusEditRequest } from 'src/app/models/OrderStatusEditRequest';
import { PageResponse } from 'src/app/models/PageResponse';
import { environment } from 'src/environments/environment';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { EventBusService } from '../event-bus/event-bus.service';

const ORDER_SERVICE_URL = environment.apiUrl + '/orders';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class OrderService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	public getOrdersPaginated(
		page: number,
		pageSize: number,
		withItems: boolean = false,
		activeOnly: boolean = false,
		term: string = ''
	): Observable<PageResponse<OrderResponse>> {
		const params = new HttpParams()
			.set('page', page)
			.set('size', pageSize)
			.set('withItems', withItems)
			.set('activeOnly', activeOnly)
			.set('term', term);

		return this.http
			.get<PageResponse<OrderResponse>>(ORDER_SERVICE_URL, {
				params: params,
			})
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getOrderById(
		id: number,
		withItems: boolean = false
	): Observable<OrderResponse> {
		const params = new HttpParams().set('withItems', withItems);

		return this.http
			.get<OrderResponse>(ORDER_SERVICE_URL + `/${id}`, {
				params: params,
			})
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public createOrder(
		orderCreateRequest: OrderCreateRequest
	): Observable<OrderResponse> {
		return this.http
			.post<OrderResponse>(ORDER_SERVICE_URL, orderCreateRequest, httpOptions)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public editOrderStatus(
		request: OrderStatusEditRequest
	): Observable<OrderResponse> {
		return this.http
			.put<OrderResponse>(
				ORDER_SERVICE_URL + `/${request.id}`,
				request,
				httpOptions
			)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
