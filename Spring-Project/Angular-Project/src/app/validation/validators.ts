import { ValidatorFn, Validators } from '@angular/forms';

export default class CustomValidators {
	static usernameValidator: ValidatorFn[] = [
		Validators.required,
		Validators.minLength(5),
		Validators.maxLength(30),
	];

	static fullNameValidator: ValidatorFn[] = [
		Validators.required,
		Validators.pattern(
			'^[A-ZÍŰÁÉÚŐÓÜÖ][a-zíűáéúőóüö]+( [A-ZÍŰÁÉÚŐÓÜÖ][a-zíűáéúőóüö]+)+$'
		),
	];

	static emailValidator: ValidatorFn[] = [Validators.required, Validators.email];

	static phoneNumberValidator: ValidatorFn[] = [
		Validators.required,
		Validators.pattern('^[0-9]{9}$'),
	];

	static passwordValidator: ValidatorFn[] = [
		Validators.required,
		Validators.pattern(
			'^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
		),
	];
}
