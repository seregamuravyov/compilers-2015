extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


struc firstClass
.num RESD 1
.flag RESD 1
.size:
endstruc



struc point
.x RESD 1
.y RESD 1
.size:
endstruc



struc vector
.a RESB point.size
.b RESB point.size
.size:
endstruc



section .text
global main

doNothing:
push ebp
mov ebp, esp
sub esp, 0
mov edx, [ebp + 8]
mov [p_doNothing_1], edx
push dword [p_doNothing_1]
pop eax
mov [l_doNothing_1], eax
mov esp, ebp
pop ebp
ret 4



getSqDistance:
push ebp
mov ebp, esp
sub esp, 4
mov edx, [ebp + 8]
mov [v_getSqDistance_2], edx
push dword [v_getSqDistance_2 + vector.a + point.x]
push dword [v_getSqDistance_2 + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
push dword [v_getSqDistance_2 + vector.a + point.x]
push dword [v_getSqDistance_2 + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
push dword [v_getSqDistance_2 + vector.a + point.y]
push dword [v_getSqDistance_2 + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
push dword [v_getSqDistance_2 + vector.a + point.y]
push dword [v_getSqDistance_2 + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 4



getBean:
push ebp
mov ebp, esp
sub esp, 4
push 0
pop eax
mov [t_getBean_3 + firstClass.flag], eax
push 6
pop eax
mov [t_getBean_3 + firstClass.num], eax
push dword [t_getBean_3 + firstClass.flag]
push dword [globIntVar]
push 0
pop ebx
pop edx
cmp edx, ebx
je  L91
mov eax, 0
jmp L92
L91:
mov eax, 1
L92:
push eax
pop ebx
pop edx
cmp edx, 1
je L79
cmp ebx, 1
je L79
mov eax, 1
jmp L80
L79:
mov eax, 0
L80:
push eax
pop eax
mov [ebp - 4], eax
push dword [t_getBean_3]
pop eax
mov esp, ebp
pop ebp
ret



main:
push ebp
mov ebp, esp
sub esp, 20
push 5
pop eax
mov [globIntVar], eax
call getBean
push eax
pop eax
mov [c_main_4], eax
push dword [c_main_4]
call doNothing
push 0
pop eax
mov [x_main_4 + point.y], eax
push 0
pop eax
mov [x_main_4 + point.x], eax
push 5
pop eax
mov [y_main_4 + point.y], eax
push 5
pop eax
mov [y_main_4 + point.x], eax
push dword [x_main_4]
pop eax
mov [v_main_4 + vector.b], eax
push dword [y_main_4]
pop eax
mov [v_main_4 + vector.a], eax
push dword [v_main_4]
call getSqDistance
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
push int_format
call printf
add esp, 8
push 2
pop eax
mov [ebp - 8], eax
push 6
pop eax
mov [ebp - 12], eax
push tmp1
push str_format
call printf
add esp, 8
lea ebx, [ebp - 12]
push ebx
push int_format
call scanf
add esp, 8
push tmp2
push str_format
call printf
add esp, 8
lea ebx, [ebp - 8]
push ebx
push int_format
call scanf
add esp, 8
push dword [ebp - 12]
push dword [ebp - 8]
call power
push eax
push int_format
call printf
add esp, 8
push tmp3
pop eax
mov [ebp - 16], eax
push tmp4
pop eax
mov [ebp - 20], eax
push dword [ebp - 16]
push dword [ebp - 20]
call strcmp
add esp, 8
mov ebx, eax
cmp ebx, 0
jne L311
mov eax, 0
jmp 312
L311:
mov eax, 1
L312:
push eax
pop eax
cmp eax, 1
jne L303
push dword [ebp - 16]
push dword [ebp - 20]
pop ebx
pop edx
push ebx
push edx
call strcat
add esp, 8
push eax
push str_format
call printf
add esp, 8
L303:
push 0
pop eax
mov esp, ebp
pop ebp
ret



power:
push ebp
mov ebp, esp
sub esp, 4
push 1
pop eax
mov [ebp - 4], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L353
mov eax, 0
jmp L354
L353:
mov eax, 1
L354:
push eax
pop eax
cmp eax, 1
jne L347
L348:
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
mov eax, edx
push eax
push 1
pop ebx
pop edx
cmp edx, ebx
je  L369
mov eax, 0
jmp L370
L369:
mov eax, 1
L370:
push eax
pop eax
cmp eax, 1
jne L361
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L361:
push dword [ebp + 12]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp + 12], eax
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
push eax
pop eax
mov [ebp + 8], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L409
mov eax, 0
jmp L410
L409:
mov eax, 1
L410:
push eax
pop eax
cmp eax, 1
je L348
L347:
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 8



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0
tmp1: dd "Enter n: ", 0
tmp2: dd "Enter k: ", 0
tmp3: dd "hello, ", 0
tmp4: dd "motherfuckers!", 0

section .bss
glob: resd 1
motherfucker: resb firstClass.size
p_doNothing_1: resb firstClass.size
l_doNothing_1: resb firstClass.size
v_getSqDistance_2: resb vector.size
t_getBean_3: resb firstClass.size
c_main_4: resb firstClass.size
x_main_4: resb point.size
y_main_4: resb point.size
v_main_4: resb vector.size
