import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingService } from './procedure-booking.service';

@Component({
    selector: 'jhi-procedure-booking-detail',
    templateUrl: './procedure-booking-detail.component.html'
})
export class ProcedureBookingDetailComponent implements OnInit, OnDestroy {

    procedureBooking: ProcedureBooking;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private procedureBookingService: ProcedureBookingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcedureBookings();
    }

    load(id) {
        this.procedureBookingService.find(id).subscribe((procedureBooking) => {
            this.procedureBooking = procedureBooking;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'procedureBookingListModification',
            (response) => this.load(this.procedureBooking.id)
        );
    }
}
