from abc import ABC, abstractmethod
# ---- Account (Abstract) ----
class Account(ABC):
    def __init__(self, acc_no, owner, balance=0):
        self.acc_no = acc_no
        self.owner = owner
        self.balance = balance
        self.transactions = []  # store history

    def deposit(self, amount):
        self.balance += amount
        self.transactions.append( "Deposit", amount)
        print(f" Deposited {amount}. New Balance = {self.balance}")

    def withdraw(self, amount):
        if amount > self.balance:
            print("Insufficient funds!")
        else:
            self.balance -= amount
            self.transactions.append((datetime.now(), "Withdraw", amount))
            print(f"Withdrawn {amount}. New Balance = {self.balance}")

    def view_transactions(self):
        print("\n--- Transaction History ---")
        for t in self.transactions:
            print(f"{t[0]} | {t[1]} | ₹{t[2]}")
        print("---------------------------")

    def show_balance(self):
        print(f"Account {self.acc_no} Balance = {self.balance}")

    @abstractmethod
    def account_type(self):
        pass


# ---- Savings Account ----
class SavingsAccount(Account):
    def account_type(self):
        return "Savings"


# ---- Current Account ----
class CurrentAccount(Account):
    def account_type(self):
        return "Current"


# ---- Bank System with Login ----
class BankSystem:
    def __init__(self):
        self.accounts = {}  # acc_no → Account
        self.users = {"admin": "1234"}  # simple login users

    def login(self, username, password):
        if self.users.get(username) == password:
            print("Login Successful!")
            return True
        print("Invalid credentials")
        return False

    def create_account(self, acc_no, owner, acc_type):
        if acc_type == "savings":
            self.accounts[acc_no] = SavingsAccount(acc_no, owner)
        elif acc_type == "current":
            self.accounts[acc_no] = CurrentAccount(acc_no, owner)
        else:
            print("Invalid account type!")
            return
        print(f"{acc_type.capitalize()} Account Created for {owner} with Acc No {acc_no}")

    def get_account(self, acc_no):
        return self.accounts.get(acc_no, None)


# ---- Menu ----
class Menu:
    def __init__(self):
        self.bank = BankSystem()

    def run(self):
        print("Welcome to Bank Management System")
        username = input("Enter username: ")
        password = input("Enter password: ")

        if not self.bank.login(username, password):
            return

        while True:
            print("\n--- Menu ---")
            print("1. Create Account")
            print("2. Deposit")
            print("3. Withdraw")
            print("4. View Balance")
            print("5. View Transactions")
            print("6. Exit")

            choice = input("Enter choice: ")

            if choice == "1":
                acc_no = input("Enter Account No: ")
                name = input("Enter Name: ")
                acc_type = input("Enter Account Type (savings/current): ").lower()
                self.bank.create_account(acc_no, name, acc_type)

            elif choice == "2":
                acc_no = input("Enter Account No: ")
                amount = float(input("Enter Amount: "))
                acc = self.bank.get_account(acc_no)
                if acc: acc.deposit(amount)
                else: print(" Account not found!")

            elif choice == "3":
                acc_no = input("Enter Account No: ")
                amount = float(input("Enter Amount: "))
                acc = self.bank.get_account(acc_no)
                if acc: acc.withdraw(amount)
                else: print("Account not found!")

            elif choice == "4":
                acc_no = input("Enter Account No: ")
                acc = self.bank.get_account(acc_no)
                if acc: acc.show_balance()
                else: print("Account not found!")

            elif choice == "5":
                acc_no = input("Enter Account No: ")
                acc = self.bank.get_account(acc_no)
                if acc: acc.view_transactions()
                else: print("Account not found!")

            elif choice == "6":
                print("Exiting... Thank you!")
                break

            else:
                print("Invalid Choice")


# ---- Run the Program ----
if __name__ == "__main__":
    Menu().run()
