import { EventAmount } from './EventAmount';
export class EventBuy{
    eventId:string;
    amount:number;
    price:number;
    constructor(ea:EventAmount){
        this.amount=ea.amount;
        this.price=ea.price;
        this.eventId=ea.id;
    }
}