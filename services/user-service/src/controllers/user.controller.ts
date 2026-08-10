import { Request, Response } from 'express';
import { userService } from '../services/user.service';
import { asyncHandler } from '../utils/asyncHandler';
import { successResponse } from '../utils/apiResponse';

export const getAll = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.getAll();
    successResponse(res, 200, 'Retrieved All User Details Successfully', data);
});

export const getById = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.getById(req.params.id as string);
    successResponse(res, 200, 'Retrieved User Details Successfully', data);
});

export const register = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.register(req.body);
    successResponse(res, 201, 'User Created Successfully', data);
});

export const login = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.login(req.body.email, req.body.password);
    successResponse(res, 200, 'User Authenticated Successfully', data);
});

export const update = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.update(req.params.id as string, req.body);
    successResponse(res, 200, 'User Updated Successfully', data);
});

export const remove = asyncHandler(async (req: Request, res: Response) => {
    await userService.delete(req.params.id as string);
    successResponse(res, 200, 'User Deleted Successfully', null);
});

export const getBookings = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.getBookings(req.params.id as string);
    successResponse(res, 200, 'Retrieved User Booking History Successfully', data);
});

export const addBooking = asyncHandler(async (req: Request, res: Response) => {
    const data = await userService.addBooking(req.params.id as string, req.body);
    successResponse(res, 201, 'User Booking Added Successfully', data);
});
