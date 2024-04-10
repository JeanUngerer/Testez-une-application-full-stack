import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { SessionApiService } from '../../services/session-api.service';
import { ListComponent } from './list.component';
import {of} from "rxjs";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  const mockSessionService = {
    sessionInformation: {
      token: 'someToken',
      type: 'someType',
      id: 1,
      username: 'someUsername',
      firstName: 'someFirstName',
      lastName: 'someLastName',
      admin: true
    }
  }

  const mockSessionApiService = {
    all: jest.fn(),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call sessionApiService.all', () => {
    mockSessionApiService.all.mockReturnValue(of(null));
    component.sessions$ = mockSessionApiService.all();
    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  it('should get user session information', () => {
    const userSession = component.user;
    expect(userSession).toEqual(mockSessionService.sessionInformation);
  });
});
