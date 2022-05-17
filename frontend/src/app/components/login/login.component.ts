import { Component, OnDestroy, OnInit, Type } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-regular-svg-icons';
import { Subscription } from 'rxjs';
import { EventData } from 'src/app/models/EventData';
import { JwtResponse } from 'src/app/models/JwtResponse';
import { LoginRequest } from 'src/app/models/LoginRequest';
import { AuthService } from 'src/app/services/auth/auth.service';
import { EventBusService } from 'src/app/services/event-bus/event-bus.service';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, OnDestroy {
	loginForm: FormGroup = this.formBuilder.group({
		username: ['', [Validators.required]],
		password: ['', [Validators.required]],
	});

	errorMessage?: String;
	isLoading: boolean = false;

	private subscriptionList: Subscription[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private authService: AuthService,
		private userDetailsStorageService: UserDetailsStorageService,
		private router: Router,
		iconLibrary: FaIconLibrary,
		private eventBusService: EventBusService
	) {
		iconLibrary.addIcons(faEyeSlash, faEye);
	}

	ngOnInit(): void {
		if (this.userDetailsStorageService.isUserLoggedIn()) {
			this.redirectToHome();
		}
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	onSubmit() {
		this.isLoading = true;

		const request: LoginRequest = {
			username: this.username?.value,
			password: this.password?.value,
		} as LoginRequest;

		this.subscriptionList.push(
			this.authService.login(request).subscribe({
				next: (resultJwtToken: JwtResponse) => {
					this.userDetailsStorageService.saveToken(resultJwtToken.token);
					this.userDetailsStorageService.saveRefreshToken(
						resultJwtToken.refreshToken
					);
					this.userDetailsStorageService.saveUser(resultJwtToken.user);

					this.isLoading = false;

					this.eventBusService.emit(new EventData('login', null));
					this.redirectToHome();
				},
				error: (error) => {
					this.errorMessage =
						'Sikertelen bejelentkezés: Helytelen bejelentkezési adatok.';
					this.isLoading = false;
				},
			})
		);
	}

	private redirectToHome() {
		this.router.navigate(['']);
	}

	get username(): AbstractControl | null {
		return this.loginForm.get('username');
	}

	get password(): AbstractControl | null {
		return this.loginForm.get('password');
	}
}
