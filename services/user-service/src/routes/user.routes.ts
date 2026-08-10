import { Router } from 'express';
import * as userController from '../controllers/user.controller';
import { validate } from '../middlewares/validate.middleware';
import { bookingLogSchema, createUserSchema, loginSchema, updateUserSchema, userIdSchema } from '../schemas/user.schemas';

const router = Router();

router.get('/', userController.getAll);

router.get('/:id', validate({ params: userIdSchema }), userController.getById);

router.post('/register', validate({ body: createUserSchema }), userController.register);

router.post('/login', validate({ body: loginSchema }), userController.login);

router.put('/:id', validate({ params: userIdSchema, body: updateUserSchema }), userController.update);

router.delete('/:id', validate({ params: userIdSchema }), userController.remove);

router.get('/:id/bookings', validate({ params: userIdSchema }), userController.getBookings);

router.post('/:id/bookings', validate({ params: userIdSchema, body: bookingLogSchema }), userController.addBooking);

export default router;
