import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
	HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { PizzaCreateRequest } from 'src/app/models/PizzaCreateRequest';
import { PizzaEditRequest } from 'src/app/models/PizzaEditRequest';
import { environment } from 'src/environments/environment';
import { PizzaResponse } from '../../models/PizzaResponse';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { EventBusService } from '../event-bus/event-bus.service';

export const PIZZA_SERVICE_URL = environment.apiUrl + '/pizzas';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class PizzaService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	getAllPizzas(): Observable<PizzaResponse[]> {
		return this.http.get<PizzaResponse[]>(PIZZA_SERVICE_URL);
	}

	createPizza(
		request: PizzaCreateRequest,
		image: File
	): Observable<PizzaResponse> {
		const formData: FormData = new FormData();
		formData.append('name', request.name);
		formData.append('description', request.description);
		formData.append('price', request.price.toString());
		formData.append('file', image);

		return this.http
			.post<PizzaResponse>(PIZZA_SERVICE_URL, formData)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	editPizza(request: PizzaEditRequest, image?: File): Observable<PizzaResponse> {
		const formData: FormData = new FormData();
		formData.append('id', request.id.toString());

		if (request.name) {
			formData.append('name', request.name);
		}

		if (typeof request.description !== 'undefined') {
			formData.append('description', request.description);
		}

		if (typeof request.price !== 'undefined' && !isNaN(request.price)) {
			formData.append('price', request.price.toString());
		}

		if (image) {
			formData.append('file', image);
		}

		return this.http
			.put<PizzaResponse>(PIZZA_SERVICE_URL + `/${request.id}`, formData)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	deletePizzaById(id: number): Observable<any> {
		return this.http
			.delete(PIZZA_SERVICE_URL + `/${id}`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
