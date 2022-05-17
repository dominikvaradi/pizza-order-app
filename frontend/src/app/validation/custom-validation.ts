import {
	AbstractControl,
	ValidationErrors,
	ValidatorFn,
	Validators,
} from '@angular/forms';

export default class CustomValidation {
	static match(controlName: string, checkControlName: string): ValidatorFn {
		return (controls: AbstractControl) => {
			const control = controls.get(controlName);
			const checkControl = controls.get(checkControlName);

			if (control?.value === checkControl?.value) {
				return null;
			} else {
				return { match: true };
			}
		};
	}

	static password(control: AbstractControl): ValidationErrors | null {
		const regex: RegExp =
			/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&_.])[A-Za-z\d@$!%*?&_.]{8,}$/g;
		const forbidden = !regex.test(control.value);
		return forbidden ? { password: { value: control.value } } : null;
	}

	static phoneNumber(control: AbstractControl): ValidationErrors | null {
		const regex: RegExp = /^[0-9]{9}$/g;
		const forbidden = !regex.test(control.value);
		return forbidden ? { phonenumber: { value: control.value } } : null;
	}

	static fullName(control: AbstractControl): ValidationErrors | null {
		const regex: RegExp =
			/^[A-ZÍŰÁÉÚŐÓÜÖ][a-zíűáéúőóüö]+( [A-ZÍŰÁÉÚŐÓÜÖ][a-zíűáéúőóüö]+)+$/g;
		const forbidden = !regex.test(control.value);
		return forbidden ? { fullname: { value: control.value } } : null;
	}

	static email(control: AbstractControl): ValidationErrors | null {
		const regex: RegExp =
			/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/g;
		const forbidden = !regex.test(control.value);
		return forbidden ? { email: { value: control.value } } : null;
	}
}
