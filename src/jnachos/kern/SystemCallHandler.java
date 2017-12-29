/**
 * Copyright (c) 1992-1993 The Regents of the University of California.
 * All rights reserved.  See copyright.h for copyright notice and limitation 
 * of liability and disclaimer of warranty provisions.
 *  
 *  Created by Patrick McSweeney on 12/5/08.
 */
package jnachos.kern;

import jnachos.machine.*;
import jnachos.filesystem.*;

/** The class handles System calls made from user programs. */
public class SystemCallHandler {
	/** The System call index for halting. */
	public static final int SC_Halt = 0;

	/** The System call index for exiting a program. */
	public static final int SC_Exit = 1;

	/** The System call index for executing program. */
	public static final int SC_Exec = 2;

	/** The System call index for joining with a process. */
	public static final int SC_Join = 3;

	/** The System call index for creating a file. */
	public static final int SC_Create = 4;

	/** The System call index for opening a file. */
	public static final int SC_Open = 5;

	/** The System call index for reading a file. */
	public static final int SC_Read = 6;

	/** The System call index for writting a file. */
	public static final int SC_Write = 7;

	/** The System call index for closing a file. */
	public static final int SC_Close = 8;

	/** The System call index for forking a forking a new process. */
	public static final int SC_Fork = 9;

	/** The System call index for yielding a program. */
	public static final int SC_Yield = 10;

	/**
	 * Entry point into the Nachos kernel. Called when a user program is
	 * executing, and either does a syscall, or generates an addressing or
	 * arithmetic exception.
	 * 
	 * For system calls, the following is the calling convention:
	 * 
	 * system call code -- r2 arg1 -- r4 arg2 -- r5 arg3 -- r6 arg4 -- r7
	 * 
	 * The result of the system call, if any, must be put back into r2.
	 * 
	 * And don't forget to increment the pc before returning. (Or else you'll
	 * loop making the same system call forever!
	 * 
	 * @pWhich is the kind of exception. The list of possible exceptions are in
	 *         Machine.java
	 **/
	/**
	 * @param pWhichSysCall
	 */
	public static void handleSystemCall(int pWhichSysCall) {
		

		System.out.println("SysCall:" + pWhichSysCall);
		System.out.println("   Process ID: " + JNachos.getCurrentProcess().getpid());

		//increment the program counter
		Machine.writeRegister(Machine.PCReg, Machine.mRegisters[Machine.NextPCReg]);
		Machine.writeRegister(Machine.NextPCReg, Machine.mRegisters[Machine.NextPCReg] + 4);

		switch (pWhichSysCall) {
		// If halt is received shut down
		case SC_Halt:
			Debug.print('a', "Shutdown, initiated by user program.");
			Interrupt.halt();
			break;

		case SC_Exit:
			// Read in any arguments from the 4th register
			int arg = Machine.readRegister(4);
			System.out.println("Current Process " + JNachos.getCurrentProcess().getName() + " exiting with code " + arg);
			// Check if the exited program has mapped waiting process
			// If so, put it back to ready queue and pass return value of Join()
			Scheduler.invokeWaitingProcess(JNachos.getCurrentProcess().getpid(), arg);
			// Finish the invoking process
			JNachos.getCurrentProcess().finish();
			
			break;
			
				
		case SC_Fork:
			  
			//disable interrupts
			System.out.println("Fork is calling process : "+ JNachos.getCurrentProcess().getpid());
 
			boolean oldLevel = Interrupt.setLevel(false);

			 Machine.writeRegister(2, 0);
			 
			 //create a new JNachos process
			 NachosProcess child = new NachosProcess(JNachos.getCurrentProcess().getName() + "'s child");
			
			 String parentmname =  JNachos.getCurrentProcess().getName();
			 int index = parentmname.lastIndexOf("/");
			 System.out.println("File name: " + parentmname.substring(index + 1));
			 
			 //copy the address space of the parent and give the child the same address space
			 
			 child.setSpace(new AddrSpace(JNachos.getCurrentProcess().getSpace()));
			 child.saveUserState();
			 Machine.writeRegister(2, child.getpid());
			 child.fork(new parentfork(), child);
             
			Interrupt.setLevel(oldLevel);
			
			break;
			
			
		case SC_Join:
			
			System.out.println("Join is being called : " + JNachos.getCurrentProcess().getpid());

            boolean oldLeveljoin=Interrupt.setLevel(false);
            
            if(Machine.readRegister(4)==JNachos.getCurrentProcess().getpid()
            		||!Scheduler.containsProcess(Machine.readRegister(4)))
            	break;
            
            
            else
            {
                System.out.println("process"+JNachos.getCurrentProcess().getpid() + Machine.readRegister(4) + "to finish");
                Scheduler.addWaitingProcess(Machine.readRegister(4), JNachos.getCurrentProcess());
                JNachos.getCurrentProcess().sleep();
            }

            Interrupt.setLevel(oldLeveljoin);
            
            Machine.run();
			
            break;
			
		case SC_Exec:
			
			System.out.println("Exec is being called");
			boolean oldLevel1 = Interrupt.setLevel(false);
			int sAddr = Machine.readRegister(4);
			int sValue = 1;
		    String execFilename = new String();
		    while((char) sValue!='\0') 
		    {
		        sValue = Machine.readMem(sAddr, 1);
		    	    if((char) sValue!= '\0')
		    		  execFilename += (char) sValue;
		    		
		    	    sAddr++;
		    	}
		    	
		    	
		    	OpenFile exec = JNachos.mFileSystem.open(execFilename);
		    	if(exec==null) {
		    		System.out.println("NULL EXEC");
		    	}
		    	
		    	AddrSpace newAS = new AddrSpace(exec);
		    	JNachos.getCurrentProcess().setSpace(newAS);
		    	JNachos.getCurrentProcess().getSpace().initRegisters();
		    	JNachos.getCurrentProcess().getSpace().restoreState();
		    	
		    	Interrupt.setLevel(oldLevel1);
				
		    	Machine.run();
		    	
			break;
			
			
		default:
			Interrupt.halt();
			break;
		}
	}
}
