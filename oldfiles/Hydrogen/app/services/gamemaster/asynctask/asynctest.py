import asyncio

async def my_coro(n):  
    print(f"The answer is {n}.")

async def main():  
    print("begin.")
    mytask = asyncio.create_task(my_coro(42))
    
    await mytask
    print("end.")

asyncio.run(main())