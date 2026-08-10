import express from 'express';
import cors from 'cors';
import userRoutes from './routes/user.routes';
import { errorHandler, notFoundHandler } from './middlewares/error.middleware';

const app = express();

app.use(cors({ origin: 'http://localhost:3000', credentials: true }));

app.use(express.json());

app.use('/user', userRoutes);

app.use(notFoundHandler);

app.use(errorHandler);

export default app;
