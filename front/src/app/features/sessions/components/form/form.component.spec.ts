import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {ActivatedRoute, convertToParamMap, Router} from "@angular/router";
import {of, throwError} from "rxjs";
import {Session} from "../../interfaces/session.interface";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
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
    create: jest.fn().mockImplementation(() => of(mockSession)),
    update: jest.fn().mockImplementation(() => of(mockSession)),
    delete: jest.fn(),
    participate: jest.fn().mockImplementation(() => of(mockSession)),
    unParticipate: jest.fn().mockImplementation(() => of(mockSession)),
  }

  const mockMatSnackBar = {
    open: jest.fn()
  }

  const routerMock = {
    navigate: jest.fn(),
    url: "create"
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: "1" }),
      url: [{ path: 'update' }]
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        FormBuilder
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit and create a session', () => {
    mockSessionApiService.create.mockReturnValue(of({}));
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should submit and update a session', () => {
    mockSessionApiService.update.mockReturnValue(of({}));
    component.onUpdate = true;
    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    expect(component.onUpdate).toEqual(true);
  });

  it('should handle create error', () => {
    const errorResponse = 'error';
    mockSessionApiService.create.mockReturnValue(throwError(errorResponse));
    component.submit();

    expect(mockMatSnackBar.open).toHaveBeenCalledTimes(0);
    expect(routerMock.navigate).toHaveBeenCalledTimes(0);
  });

  it('should handle update error', () => {
    const errorResponse = 'error';
    mockSessionApiService.update.mockReturnValue(throwError(errorResponse));
    component.onUpdate = true;
    component.submit();

    expect(mockMatSnackBar.open).toHaveBeenCalledTimes(0);
    expect(routerMock.navigate).toHaveBeenCalledTimes(0);
  });
});
