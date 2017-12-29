package jnachos.kern;
import jnachos.filesystem.OpenFile;
import jnachos.machine.Machine;

public class parentfork implements VoidFunctionPtr {
	@Override
	public void call(Object arg) {
		
		System.out.println("Forked child starting");
		
		//increment the program counter
		Machine.writeRegister(Machine.PCReg, Machine.mRegisters[Machine.NextPCReg]);
        Machine.writeRegister(Machine.NextPCReg, Machine.mRegisters[Machine.NextPCReg] + 4);
        
        System.out.println("Registers copied from child to machine");
        JNachos.getCurrentProcess().restoreUserState();
        JNachos.getCurrentProcess().getSpace().restoreState();
	   
	   System.out.println("Child process started");
		Machine.run();
		
		assert(false);
	}

}
