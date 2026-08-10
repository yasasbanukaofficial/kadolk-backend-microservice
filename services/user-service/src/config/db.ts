import mongoose from 'mongoose';

export const connectDB = async (): Promise<void> => {
    const uri = process.env.MONGO_URI ?? 'mongodb://localhost:27017/user-service';
    await mongoose.connect(uri);
    console.log('MongoDB connected successfully');
};
