import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormControl,
	FormGroup,
	ValidatorFn,
	Validators,
} from '@angular/forms';
import {
	faEye,
	faEyeSlash,
	IconDefinition,
} from '@fortawesome/free-regular-svg-icons';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import CustomValidation from 'src/app/validation/custom-validation';

@Component({
	selector: 'app-edit-user-modal',
	templateUrl: './edit-user-modal.component.html',
	styleUrls: ['./edit-user-modal.component.css'],
})
export class EditUserModalComponent implements OnInit {
	@Input()
	needsConfirm: boolean = false;

	@Input()
	valueLabel: string = 'Érték';

	@Input()
	initialValue: String = '';

	@Input()
	valueValidators: ValidatorFn[] = [];

	@Input()
	valueInputType: String = 'text';

	editUserForm: FormGroup = this.formBuilder.group({
		value: '',
		password: ['', [Validators.required]],
	});

	get value(): AbstractControl | null {
		return this.editUserForm.get('value');
	}

	get valueConfirm(): AbstractControl | null {
		return this.editUserForm.get('valueConfirm');
	}

	get password(): AbstractControl | null {
		return this.editUserForm.get('password');
	}

	faEyeSlashIcon: IconDefinition = faEyeSlash;
	faEyeIcon: IconDefinition = faEye;

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder
	) {}

	ngOnInit(): void {
		if (this.needsConfirm) {
			this.editUserForm.addControl(
				'valueConfirm',
				new FormControl('', [Validators.required])
			);

			this.editUserForm.addValidators(
				CustomValidation.match('value', 'valueConfirm')
			);
		}

		this.value?.setValue(this.initialValue);
		this.value?.addValidators(this.valueValidators);
	}

	onSubmit() {
		this.activeModal.close({
			value: this.value?.value,
			password: this.password?.value,
		});
	}

	closeModal() {
		this.activeModal.dismiss('Close click');
	}
}
