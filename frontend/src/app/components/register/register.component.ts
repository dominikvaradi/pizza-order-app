import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import {
	faEyeSlash,
	faEye,
	IconDefinition,
} from '@fortawesome/free-regular-svg-icons';
import { Subscription } from 'rxjs';
import { UserEditRequest } from 'src/app/models/UserEditRequest';
import { UserRegisterRequest } from 'src/app/models/UserRegisterRequest';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { UserService } from 'src/app/services/user/user.service';
import CustomValidation from 'src/app/validation/custom-validation';
import CustomValidators from 'src/app/validation/validators';

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit, OnDestroy {
	registerForm: FormGroup = this.formBuilder.group(
		{
			username: ['', CustomValidators.usernameValidator],
			fullName: ['', CustomValidators.fullNameValidator],
			email: ['', CustomValidators.emailValidator],
			phoneNumber: ['', CustomValidators.phoneNumberValidator],
			password: ['', CustomValidators.passwordValidator],
			passwordConfirm: ['', CustomValidators.passwordValidator],
		},
		{
			validators: [CustomValidation.match('password', 'passwordConfirm')],
		}
	);

	subscriptionList: Subscription[] = [];

	errorMessage?: String;
	successMessage?: String;
	isLoading: boolean = false;

	constructor(
		private formBuilder: FormBuilder,
		iconLibrary: FaIconLibrary,
		private userDetailsStorageService: UserDetailsStorageService,
		private router: Router,
		private userService: UserService
	) {
		iconLibrary.addIcons(faEyeSlash, faEye);
	}

	ngOnInit(): void {
		if (this.userDetailsStorageService.isUserLoggedIn()) {
			this.router.navigate(['']);
		}
	}

	ngOnDestroy() {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	onSubmit() {
		this.isLoading = true;

		const request: UserRegisterRequest = {
			username: this.username?.value,
			password: this.password?.value,
			email: this.email?.value,
			phoneNumber: '+36' + this.phoneNumber?.value,
			fullName: this.fullName?.value,
		};

		this.subscriptionList.push(
			this.userService.registerUser(request).subscribe({
				next: (result: UserResponse) => {
					this.isLoading = false;
					this.errorMessage = undefined;

					this.username?.reset('');
					this.password?.reset('');
					this.passwordConfirm?.reset('');
					this.email?.reset('');
					this.phoneNumber?.reset('');
					this.fullName?.reset('');
					
					this.successMessage = "Sikeresen regisztráltál!";
				},
				error: (error: HttpErrorResponse) => {
					this.isLoading = false;

					if (error.status === 409) {
						this.errorMessage = "Ilyen felhasználónévvel vagy e-mail címmel már létezik felhasználó!"
					}

					this.password?.reset('');
					this.passwordConfirm?.reset('');
				},
			})
		);
	}

	get username(): AbstractControl | null {
		return this.registerForm.get('username');
	}

	get fullName(): AbstractControl | null {
		return this.registerForm.get('fullName');
	}

	get email(): AbstractControl | null {
		return this.registerForm.get('email');
	}

	get phoneNumber(): AbstractControl | null {
		return this.registerForm.get('phoneNumber');
	}

	get password(): AbstractControl | null {
		return this.registerForm.get('password');
	}

	get passwordConfirm(): AbstractControl | null {
		return this.registerForm.get('passwordConfirm');
	}
}
