import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationSelectorComponent } from './registration-selector.component';

describe('RegistrationSelectorComponent', () => {
  let component: RegistrationSelectorComponent;
  let fixture: ComponentFixture<RegistrationSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationSelectorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
