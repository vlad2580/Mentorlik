import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMentorComponent } from './create-mentor.component';

describe('CreateMentorComponent', () => {
  let component: CreateMentorComponent;
  let fixture: ComponentFixture<CreateMentorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMentorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateMentorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
