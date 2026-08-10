import bcrypt from 'bcryptjs';
import { IBookingLog, User, UserDoc } from '../models/user.model';
import { DuplicateUserError, InvalidCredentialsError, UserNotFoundError } from '../errors/custom.errors';
import { signToken } from '../utils/jwt';
import { BookingLogInput, CreateUserInput, UpdateUserInput } from '../schemas/user.schemas';

const toUserRes = (user: UserDoc): Record<string, unknown> => {
    const { password, ...rest } = user.toObject();
    return rest;
};

const toBookingLog = (log: IBookingLog): Record<string, unknown> => ({
    parkingId: log.parkingId,
    vehicleId: log.vehicleId,
    action: log.action,
    timestamp: log.timestamp
});

export const userService = {
    async getAll(): Promise<Record<string, unknown>[]> {
        const users = await User.find().select('-password');
        return users.map((user) => toUserRes(user));
    },

    async getById(id: string): Promise<Record<string, unknown>> {
        const user = await User.findById(id);
        if (!user) {
            throw new UserNotFoundError('User not found with id: ' + id);
        }
        return toUserRes(user);
    },

    async register(data: CreateUserInput): Promise<Record<string, unknown>> {
        const exists = await User.exists({ email: data.email });
        if (exists) {
            throw new DuplicateUserError('User already exists with email: ' + data.email);
        }
        const hashedPassword = await bcrypt.hash(data.password, 10);
        const user = await User.create({ ...data, password: hashedPassword });
        return toUserRes(user);
    },

    async login(email: string, password: string): Promise<{ token: string; user: Record<string, unknown> }> {
        const user = await User.findOne({ email });
        if (!user) {
            throw new InvalidCredentialsError('Invalid email or password');
        }
        const isValid = await bcrypt.compare(password, user.password);
        if (!isValid) {
            throw new InvalidCredentialsError('Invalid email or password');
        }
        const token = signToken(user.id);
        return { token, user: toUserRes(user) };
    },

    async update(id: string, data: UpdateUserInput): Promise<Record<string, unknown>> {
        const user = await User.findById(id);
        if (!user) {
            throw new UserNotFoundError('User not found with id: ' + id);
        }
        user.name = data.name;
        user.email = data.email;
        user.phone = data.phone;
        user.role = data.role;
        user.status = data.status;
        if (data.password) {
            user.password = await bcrypt.hash(data.password, 10);
        }
        await user.save();
        return toUserRes(user);
    },

    async delete(id: string): Promise<void> {
        const user = await User.findByIdAndDelete(id);
        if (!user) {
            throw new UserNotFoundError('User not found with id: ' + id);
        }
    },

    async getBookings(id: string): Promise<Record<string, unknown>[]> {
        const user = await User.findById(id);
        if (!user) {
            throw new UserNotFoundError('User not found with id: ' + id);
        }
        return user.bookingHistory.map((log) => toBookingLog(log));
    },

    async addBooking(id: string, data: BookingLogInput): Promise<Record<string, unknown>[]> {
        const user = await User.findById(id);
        if (!user) {
            throw new UserNotFoundError('User not found with id: ' + id);
        }
        user.bookingHistory.push({ ...data, timestamp: new Date() });
        await user.save();
        return user.bookingHistory.map((log) => toBookingLog(log));
    }
};
