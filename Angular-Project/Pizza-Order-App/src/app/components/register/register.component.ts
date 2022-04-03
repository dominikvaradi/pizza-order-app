import { Component, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import {
	faEyeSlash,
	faEye,
	IconDefinition,
} from '@fortawesome/free-regular-svg-icons';
import { UserEditRequest } from 'src/app/models/UserEditRequest';
import CustomValidation from 'src/app/validation/custom-validation';
import CustomValidators from 'src/app/validation/validators';

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
	registerForm: FormGroup = this.formBuilder.group(
		{
			username: ['', CustomValidators.usernameValidator],
			fullName: ['', CustomValidators.fullNameValidator],
			email: ['', CustomValidators.emailValidator],
			phoneNumber: ['', CustomValidators.phoneNumberValidator],
			password: ['', CustomValidators.passwordValidator],
			passwordConfirm: ['', [Validators.required]],
		},
		{
			validators: [CustomValidation.match('password', 'passwordConfirm')],
		}
	);

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

	faEyeSlashIcon: IconDefinition = faEyeSlash;
	faEyeIcon: IconDefinition = faEye;

	isPasswordFieldHidden: boolean = true;
	isPasswordConfirmFieldHidden: boolean = true;

	constructor(private formBuilder: FormBuilder) {}

	ngOnInit(): void {}

	onSubmit() {
		const model: UserEditRequest = new UserEditRequest(
			NaN,
			this.username?.value,
			this.fullName?.value,
			this.email?.value,
			this.phoneNumber?.value,
			this.password?.value
		);

		// TODO Service lekezelés
		console.log(JSON.stringify(model));
	}
}
