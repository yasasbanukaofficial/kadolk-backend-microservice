import { HydratedDocument, model, Schema } from 'mongoose';

export interface IBookingLog {
    parkingId: string;
    vehicleId: string;
    action: string;
    timestamp: Date;
}

export interface IUser {
    name: string;
    email: string;
    phone: string;
    password: string;
    role: 'USER' | 'OWNER' | 'ADMIN';
    status: 'ACTIVE' | 'INACTIVE';
    bookingHistory: IBookingLog[];
    createdAt: Date;
    updatedAt: Date;
}

const bookingLogSchema = new Schema<IBookingLog>({
    parkingId: { type: String, required: true },
    vehicleId: { type: String, required: true },
    action: { type: String, required: true },
    timestamp: { type: Date, default: Date.now }
}, { _id: false });

const userSchema = new Schema<IUser>({
    name: { type: String, required: true, trim: true, maxlength: 100 },
    email: { type: String, required: true, unique: true, lowercase: true, trim: true },
    phone: { type: String, required: true, trim: true, maxlength: 15 },
    password: { type: String, required: true },
    role: { type: String, enum: ['USER', 'OWNER', 'ADMIN'], default: 'USER' },
    status: { type: String, enum: ['ACTIVE', 'INACTIVE'], default: 'ACTIVE' },
    bookingHistory: { type: [bookingLogSchema], default: [] }
}, { timestamps: true });

export const User = model<IUser>('User', userSchema);

export type UserDoc = HydratedDocument<IUser>;
