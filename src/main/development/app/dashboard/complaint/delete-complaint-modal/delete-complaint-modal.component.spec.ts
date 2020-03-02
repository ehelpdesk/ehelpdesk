import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteComplaintModalComponent } from './delete-complaint-modal.component';

describe('DeleteComplaintModalComponent', () => {
  let component: DeleteComplaintModalComponent;
  let fixture: ComponentFixture<DeleteComplaintModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteComplaintModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteComplaintModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
