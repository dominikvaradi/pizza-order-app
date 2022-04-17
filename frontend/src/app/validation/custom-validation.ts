import { AbstractControl, ValidatorFn } from '@angular/forms';

export default class CustomValidation {
	static match(controlName: string, checkControlName: string): ValidatorFn {
		return (controls: AbstractControl) => {
			const control = controls.get(controlName);
			const checkControl = controls.get(checkControlName);

			if (control?.value === checkControl?.value) {
				return null;
			} else {
				return { matchingError: true };
			}
		};
	}
}
