import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CabinetStudentComponent } from './cabinet-student.component';

describe('CabinetStudentComponent', () => {
  let component: CabinetStudentComponent;
  let fixture: ComponentFixture<CabinetStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CabinetStudentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CabinetStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
