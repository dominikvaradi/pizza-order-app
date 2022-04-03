import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPizzaModalComponent } from './edit-pizza-modal.component';

describe('EditPizzaModalComponent', () => {
  let component: EditPizzaModalComponent;
  let fixture: ComponentFixture<EditPizzaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditPizzaModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditPizzaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
