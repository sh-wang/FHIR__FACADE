import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { FollowupAction } from './followup-action.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FollowupActionService {

    private resourceUrl = SERVER_API_URL + 'api/followup-actions';
    private patientResourceUrl = SERVER_API_URL + 'api/patient/';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/followup-actions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(followupAction: FollowupAction): Observable<FollowupAction> {
        const copy = this.convert(followupAction);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(followupAction: FollowupAction): Observable<FollowupAction> {
        const copy = this.convert(followupAction);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<FollowupAction> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    findByPatientId(id: any, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.patientResourceUrl+id+'/followup-actions', options)
            .map((res: Response) => this.convertResponse(res));
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to FollowupAction.
     */
    private convertItemFromServer(json: any): FollowupAction {
        const entity: FollowupAction = Object.assign(new FollowupAction(), json);
        entity.scheduledDate = this.dateUtils
            .convertDateTimeFromServer(json.scheduledDate);
        entity.completedDate = this.dateUtils
            .convertDateTimeFromServer(json.completedDate);
        return entity;
    }

    /**
     * Convert a FollowupAction to a JSON which can be sent to the server.
     */
    private convert(followupAction: FollowupAction): FollowupAction {
        const copy: FollowupAction = Object.assign({}, followupAction);

        copy.scheduledDate = this.dateUtils.toDate(followupAction.scheduledDate);

        copy.completedDate = this.dateUtils.toDate(followupAction.completedDate);
        return copy;
    }
}
