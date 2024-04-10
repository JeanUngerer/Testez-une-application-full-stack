import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { DetailComponent } from './detail.component';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSession: Session = {
    name: 'Test Session',
    description: 'This is a test session',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2, 3],
  };

  const mockSessionApiService = {
    detail: jest.fn().mockImplementation(() => of(mockSession)),
    delete: jest.fn(),
    participate: jest.fn().mockImplementation(() => of(mockSession)),
    unParticipate: jest.fn().mockImplementation(() => of(mockSession)),
  }

  const mockTeacherService = {
    detail: jest.fn()
  }

  const mockMatSnackBar = {
    open: jest.fn()
  }

  const routerMock = {
    navigate: jest.fn(),
  };

  const mockActivatedRoute = {
    params: convertToParamMap({ id: 1 }),
    snapshot: {
      paramMap: convertToParamMap({ id: "1" })
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],

    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks(); // reset all mocks after each test
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should call window.history.back', () => {
    jest.spyOn(window.history, 'back');

    component.back();

    expect(window.history.back).toHaveBeenCalled();
  });



  it('should call sessionApiService.delete and navigate', () => {
    mockSessionApiService.delete.mockReturnValue(of(null));
    component.delete();

    expect(mockSessionApiService.delete).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });

  it('should handle delete error', () => {
    const errorResponse = 'error';
    mockSessionApiService.delete.mockReturnValue(throwError(errorResponse));

    component.delete();

    expect(routerMock.navigate).toHaveBeenCalledTimes(0);
    expect(mockMatSnackBar.open).toHaveBeenCalledTimes(0);
  });



  it('should call sessionApiService.participate and fetchSession', () => {
    mockSessionApiService.participate.mockReturnValue(of(null));

    component.participate();

    expect(mockSessionApiService.participate).toHaveBeenCalled();
  });



  it('should call sessionApiService.unParticipate and fetchSession', () => {
    mockSessionApiService.unParticipate.mockReturnValue(of(null));

    component.unParticipate();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalled();
  });

});
