import { ValidatorFn, Validators } from '@angular/forms';
import CustomValidation from './custom-validation';

export default class CustomValidators {
	static usernameValidator: ValidatorFn[] = [
		Validators.required,
		Validators.minLength(5),
		Validators.maxLength(30),
	];

	static fullNameValidator: ValidatorFn[] = [
		Validators.required,
		CustomValidation.fullName,
	];

	static emailValidator: ValidatorFn[] = [
		Validators.required,
		CustomValidation.email,
	];

	static phoneNumberValidator: ValidatorFn[] = [
		Validators.required,
		CustomValidation.phoneNumber,
	];

	static passwordValidator: ValidatorFn[] = [
		Validators.required,
		CustomValidation.password,
	];
}
