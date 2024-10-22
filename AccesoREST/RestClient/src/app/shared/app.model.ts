import { PropertyWrite } from "@angular/compiler"

export interface Usuario {
    id: Number,
    name: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    paymentCard: String,
    enabled: Boolean,
}

export interface Vehiculo{
    id: Number,
    carRegistration: String,
    userId: Number,
    brand: String,
    model: String,
    capacity: Number,
    plugType: PlugType
}

export interface PuntoDeCarga{
    id: Number,
    address: String,
    latitude: number,
    longitude: number,
    plugType: PlugType,
    power: Power,
    status: Status
}

export interface Recarga{
    id: Number,
    userId: Number,
    vehicleId: Number,
    chargerpointId: Number,
    price: Number,
    kw: Number,
    status: RechargeStatus,
    payment: PaymentStatus
}

export enum PlugType{
    SCHUKO = 'SCHUKO',
    CSS = 'CSS',
    MENNEKES = 'MENNEKES',
    CHADEMO = 'CHADEMO'
}

export enum Power{
    LENTA = 'LENTA',
    MEDIA = 'MEDIA',
    RAPIDA = 'RAPIDA',
    ULTRARAPIDA = 'ULTRARAPIDA'

}

export enum Status{
    MANTENIMIENTO = 'MANTENIMIENTO',
    DISPONIBLE = 'DISPONIBLE',
    EN_SERVICIO = 'EN_SERVICIO'
}

export enum RechargeStatus{
  NOTSTARTED = "NOTSTARTED",
  CHARGING = "CHARGING",
  COMPLETED = "COMPLETED"
}

export enum PaymentStatus{
  NOTPROCESSED = "NOTPROCESSED",
  CANCELLED = "CANCELLED",
  PENDING = "PENDING",
  COMPLETED = "COMPLETED"
}
