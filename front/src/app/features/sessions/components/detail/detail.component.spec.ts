import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import {ActivatedRoute, Router} from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

import { DetailComponent } from './detail.component';
import * as Cypress from "cypress";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSessionApiService = {
    detail: jest.fn(),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn()
  }

  const mockTeacherService = {
    detail: jest.fn()
  }

  const routerMock= {
    navigate: jest.fn(),
  };

  const mockActivatedRoute = {
    route: jest.fn(),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],

    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('#back', () => {
    it('should call window.history.back', () => {
      jest.spyOn(window.history, 'back');

      component.back();

      expect(window.history.back).toHaveBeenCalled();
    });
  });

  describe('#delete', () => {
    it('should call sessionApiService.delete and navigate', () => {
      mockSessionApiService.delete.mockReturnValue(of(null));

      component.delete();

      expect(mockSessionApiService.delete).toHaveBeenCalled();
      expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should handle delete error', () => {
      const errorResponse = 'error';
      mockSessionApiService.delete.mockReturnValue(throwError(errorResponse));

      component.delete();

      expect(routerMock.navigate).toHaveBeenCalledTimes(0);
    });
  });

  describe('#participate', () => {
    it('should call sessionApiService.participate and fetchSession', () => {
      mockSessionApiService.participate.mockReturnValue(of(null));

      component.participate();

      expect(mockSessionApiService.participate).toHaveBeenCalled();
    });
  });

  describe('#unParticipate', () => {
    it('should call sessionApiService.unParticipate and fetchSession', () => {
      mockSessionApiService.unParticipate.mockReturnValue(of(null));

      component.unParticipate();

      expect(mockSessionApiService.unParticipate).toHaveBeenCalled();
    });
  });
});
