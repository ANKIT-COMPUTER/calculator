package com.example.adroidapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.Stack;
import android.widget.TextView; 
public class MainActivity extends Activity {
    public
    TextView resultTv,solutionTv;
    Button buttonC,buttonBrackOpen,buttonBrackClose;
    Button buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals;
    Button button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    Button buttonAC,buttonDot;
    int n=0;
  
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	resultTv  = (TextView)findViewById(R.id.result_tv);
		solutionTv = (TextView)findViewById(R.id.solution_tv);
		  assignId(buttonC,R.id.button_c);
	       assignId(buttonBrackOpen,R.id.button_open_bracket);
	       assignId(buttonBrackClose,R.id.button_close_bracket);
	       assignId(buttonDivide,R.id.button_divide);
	       assignId(buttonMultiply,R.id.button_multiply);
	       assignId(buttonPlus,R.id.button_plus);
	       assignId(buttonMinus,R.id.button_minus);
	       assignId(buttonEquals,R.id.button_equals);
	       assignId(button0,R.id.button_0);
	       assignId(button1,R.id.button_1);
	       assignId(button2,R.id.button_2);
	       assignId(button3,R.id.button_3);
	       assignId(button4,R.id.button_4);
	       assignId(button5,R.id.button_5);
	       assignId(button6,R.id.button_6);
	       assignId(button7,R.id.button_7);
	       assignId(button8,R.id.button_8);
	       assignId(button9,R.id.button_9);
	       assignId(buttonAC,R.id.button_ac);
	       assignId(buttonDot,R.id.button_dot);





	    }

	    void assignId(Button btn,int id){
	        btn = (Button) findViewById(id);
	        btn.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		 Button button =(Button) view;
	        	        String buttonText = button.getText().toString();
	        	        String dataToCalculate = solutionTv.getText().toString();

	        	        if(buttonText.equals("AC")){
	        	            solutionTv.setText("");
	        	            resultTv.setText("0");
	        	            return;
	        	        }
	        	        if(buttonText.equals("=")){
	        	        	
	 	        	        solutionTv.setText(resultTv.getText());
	 	        	        return;
	 	        	        
	        	        }
	        	        if(buttonText.equals("C")){
	        	        	try{
	        	        	if(dataToCalculate.length()>=2){
	        	            dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
	        	            solutionTv.setText(dataToCalculate);
	        	            double finalR=evaluate(dataToCalculate);
	 	        	        String result=Double.toString(finalR);
	 	        	        resultTv.setText(result);
	        	        	}
	        	        	else{
	        	        		resultTv.setText("0");
	        	        		solutionTv.setText("");
	        	        	}
	        	        	}
	        	        	catch(Exception e){
	        	            	toastMessage("invalid input");
	        	            	
	        	            }
	        	          } 
	        	        else if(buttonText.equals("(")){
	        	            dataToCalculate = dataToCalculate+buttonText; 
	        	            solutionTv.setText(dataToCalculate);
	        	        }
	        	       
	        	        else{
	        	            dataToCalculate = dataToCalculate+buttonText; 
	        	            solutionTv.setText(dataToCalculate);
	        	            try{
	        	            double finalR=evaluate(dataToCalculate);
	 	        	        String result=Double.toString(finalR);
	 	        	        resultTv.setText(result);
	        	            }
	        	            catch(Exception e){
	        	            	toastMessage("invalid input");
	        	            	
	        	            }
	        	        }
	        	        
	        	       
	        		
	        		
	        	}
	        });
	    }

		public  double evaluate(String expression)
		
		{ 
			if(areBracketsBalanced(expression))
			{	
			if(expression=="0" || expression==" "){
			return 0;
		}
			char[] tokens = expression.toCharArray();
			String operators = "+-*/";
			int j=tokens.length-1;
			while(tokens[j]=='+' ||tokens[j]=='-'||tokens[j]=='*'||tokens[j]=='/'||tokens[j]=='.'){
				tokens[j]=' ';
				j--;
				
			}
			

			// Stack for numbers: 'values'
			Stack<Double> values = new Stack<Double>();

			// Stack for Operators: 'ops'
			Stack<Character> ops = new Stack<Character>();
			
			for (int i = 0; i < tokens.length; i++)
			{
				// Current token is a whitespace, skip it
				if (tokens[i] == ' ')
					continue;

				// Current token is a number,
				// push it to stack for numbers
				if (Character.isDigit(tokens[i]) ||tokens[i]=='.')
				{
					StringBuffer valueBuffer = new StringBuffer();
					
					// There may be more than one digits in number
					while (i < tokens.length &&( Character.isDigit(tokens[i]) || tokens[i]=='.')) 
						valueBuffer.append(tokens[i++]);
					values.push(Double.parseDouble(valueBuffer.toString()));
					// As we have move pointer ahead  we need to move it back once
					i--;
				}

				// Current token is an opening brace, push it to 'ops'
				else if (tokens[i] == '(')
					ops.push(tokens[i]);

				// Closing brace encountered, let's solve entire brace by poping out elements from ops and values till we get opening bracket
				else if (tokens[i] == ')')
				{
					while (ops.peek() != '(')
						values.push(applyOp(ops.pop(), values.pop(), values.pop()));
					ops.pop();
				}

				// Current token is an operator.
				else if (operators.indexOf(tokens[i])!=-1)
				{
					// While top of 'ops' has same or greater precedence to current token, which is an operator.
					// Apply operator on top of 'ops' to top two elements in values stack
					while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) 
						values.push(applyOp(ops.pop(), values.pop(), values.pop()));
					
					// Push current token to 'ops'.
					ops.push(tokens[i]);
				}
			}

			// Entire expression has been parsed at this point, apply remaining ops to remaining values
			while (!ops.empty())
				values.push(applyOp(ops.pop(), values.pop(), values.pop()));

			// Top of 'values' contains result, return it
			return values.pop();
		}
		else{
			return 0;
		}
}

		// Returns true if 'op2' has higher
		// or same precedence as 'op1',
		// otherwise returns false.
		public  boolean hasPrecedence(
							char op1, char op2)
		{
			if (op2 == '(' || op2 == ')')
				return false;
			if ((op1 == '*' || op1 == '/') &&
				(op2 == '+' || op2 == '-'))
				return false;
		    return true;
		}

		// A utility method to apply an operator to operand a and b
		public double applyOp(char op, double b, double a)
		{
			switch (op)
			{
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/':
				if (b == 0)
					return 0;
				else	
				return a / b;
			}
			return 0;
		}
		boolean areBracketsBalanced(String s)
	    {
			Stack<Character> stack  = new Stack<Character>();
	        for(int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            if(c == '[' || c == '(' || c == '{' ) {     
	                stack.push(c);
	            } else if(c == ']') {
	                if(stack.isEmpty() || stack.pop() != '[') {
	                    return false;
	                }
	            } else if(c == ')') {
	                if(stack.isEmpty() || stack.pop() != '(') {
	                    return false;
	                }           
	            } else if(c == '}') {
	                if(stack.isEmpty() || stack.pop() != '{') {
	                    return false;
	                }
	            }

	        }
	        return stack.isEmpty();
	        
	    }
		private void toastMessage(String message) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
    }
